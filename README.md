### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.

1. Tomcat 서버가 시작하면서 Servlet Container를 초기화한다. Servlet Container는 Servlet을 라이프 사이클에 따라 생성 및 관리하는 객체이다. 또한 사용자 요청과 Servlet을 매핑해주고, 세션을 관리하며, 동시에 여러 개의 요청을 처리하기 위한 쓰레드 관리 등을 한다.
2. Servlet Container는 Servlet Context를 초기화한다. Servlet Context는 각 Servlet과 Servlet Container가 상호 작용 하기 위한 인터페이스이며 웹 애플리케이션 전체가 공유하는 환경을 말한다. 웹 애플리케이션은 하나의 Servlet Context만 가지며, Servlet Context는 웹 애플리케이션이 종료될 때까지 살아있다. 
3. `@WebListener`로 등록된 `ServletContextListener`를 구현하고 있는 `ContextLoaderListener`가 실행된다. Listener 객체는 이벤트 발생 시 실행되는 객체인데, `ServletContextListener`는 Servlet Context가 초기화되고 파괴될 때 실행되는 메소드를 정의하기 위한 인터페이스이다. Servlet Context가 생성되면 `contextInitialized()`가 실행되고, Servlet Context가 파괴되면 `contextDestroyed()`가 실행된다. `ContextLoaderListener.contextInitialized()`는 `jwp.sql`을 실행시켜 데이터베이스를 초기화하고, `@Component`가 달린 클래스들을 찾아 Servlet Context에 등록시킨다.
4. `DispatcherServlet`이 초기화된다. `DispatcherServlet`에는 `@WebServlet(..., urlPatterns = "/", loadOnStartup = 1)`가 달려 있는데, `loadOnStartup = 1`인 경우 웹 애플리케이션이 시작하자마자 해당 Servlet을 Servlet Container에 로드한다. `DispatcherServlet`은 프론트 컨트롤러로 서버로 들어오는 모든 요청은 해당 Servlet으로 가게 된다. `DispatcherServlet`은 `getHandler()`를 통해 요청에 따른 알맞은 핸들러를 찾는데, 이를 위해 `DispatcherServlet` 초기화 시 `HandlerMapping` 객체를 초기화 시킨다. `HandlerMapping`의 구현체에는 `RequestMappingHandlerMapping`이 있는데 이는 Servlet Context에 등록된 컴포넌트 중에서 `@Controller`가 달린 컨트롤러를 찾고, 컨트롤러 내 메소드 중에서 `@RequestMapping`이 달린 핸들러들을 찾아 `((URI, Http Method), Handler)` 형식으로 `HashMap`에 추가한다.

### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.

1. `DispatcherServlet`은 프론트 컨트롤러로 서버에 들어오는 모든 요청을 전달 받는다. `HandlerMapping`의 `getHandler()`를 통해 요청에 따른 핸들러를 찾는다.
1. `getHandlerAdaptor()`를 통해 각 `HandlerAdaptor`의 `supports()`를 실행하며 `getHandler()`로 가져온 핸들러에 대한 `HandlerAdaptor`를 찾아 `handle()`를 통해 핸들러 실행을 위임한다. `HandlerAdaptor`는 핸들러 실행 전 `HandlerMethodArgumentResolver`로 핸들러의 인자들에 알맞은 값들을 넣어준다. 그리고 핸들러를 실행한 뒤 `HandlerMethodReturnValueHandler`로 반환 타입에 따라 알맞은 처리를 한다. `HandlerMethodReturnValueHandler`가 존재하는 이유는 `handle()`의 반환 타입이 `ModelAndView`로 고정이지만 핸들러의 실행 결과는 `String`, `View`, `ResponseEntity`로 다양하기 때문이다. `handleReturnValue()`는 인자로 전달 받은 `ModelAndView`를 적절히 조작한다.
    1. `String`인 경우 `ViewNameMethodReturnValueHandler`가 실행된다. 이는 해당 값으로 `View` 객체를 생성해 `ModelAndView`에 넣는다.
    2. `View`인 경우 `ViewMethodReturnValueHandler`가 실행된다. 이는 해당 `View`를 `ModelAndView`에 넣는다.
    3. `ModelAndView`인 경우 `ModelAndViewMethodReturnValueHandler`가 실행된다. 이는 해당 `ModelAndView`의 값으로 변경한다.
    4. `ResponseEntity`인 경우 `ResponseEntityMethodReturnValueHandler`가 실행된다. 이는 해당 값에 대해 반환 헤더와 바디 등을 설정한다.
2. `DispatcherServlet`은 `HandlerAdaptor`로 부터 반환 받은 `ModelAndView`과 `render()`를 통해 사용자에게 응답을 보낸다.


### 7. `next.web.qna` package의 `ShowController`는 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.

데이터베이스에서 적절한 트랜젝션 격리 수준을 설정해두지 않았다면 `findById` 등과 같은 메소드들의 검색 결과가 유효하지 않은 값일 수 있다. 예를 들어, 한 쓰레드에서는 ID가 1인 질문을 삭제하고 있지만 다른 쓰레드에서 ID가 1인 질문을 가져와 사용자에게 보여줄 수도 있다. 그렇게 되면 이미 삭제된 질문이지만, 사용자에게 보여지게 되어 해당 질문에 대한 답변을 다는 요청을 받게될 수도 있다.
