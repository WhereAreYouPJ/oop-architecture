# Architecture-Guide (Layerd Architecture)

## 지향 사항

### 0. TDA 원칙
객체는 단순한 데이터로서 존재하는 것이 아니라 '행동'하는 것이 중요합니다.
<br>
TDA 원칙은 '묻지 말고 시켜라'라는 말로 객체를 행동하게 만들어야합니다.
<br><br>

"자동차 클래스를 만들어줄 수 있나요?"

```java
// 개발자 A: 데이터 위주의 사고
public class Car {
	private Frame frame;
	private Engine engine;
	private List<Wheel> wheels;
	private float speed;
	private float direction;
}

// 개발자 B: 행동 위주의 사고
public interface Car {
	public void drive() {}
	public void changeDirection(float amount) {}
	public void accelerate(float speed) {}
	public void decelerate(float speed) {}
}

```
데이터 위주의 사고 방식으로 만들어진 클래스에는 필요한 속성들이 정의되어 있습니다. 즉, 어떤 객체를 정의하는데 필요한 속성으로만 묶여 있습니다. 그리고 이러한 클래스는 절차지향 언어에서 구조적인 데이터 덩어리를 만드는데 사용하는 구조체와 다를 바 없습니다. **전혀 객체지향스럽지 않은 것입니다.**
<br>
객체를 구분 짓는 요인은 데이터가 아닙니다. 행동입니다. 우리는 객체를 만들 때 데이터보다는 행동에 집중해야합니다. 데이터가 객체를 결정하지 않습니다. 행동이 객체를 결정합니다.
<br><br>

### 1. SOLID 원칙
단일 책임 원칙(SRP: Single Responsibility Princitple)
```text
클래스를 변경해야할 이유는 단 하나여야 합니다.
- 로버트 C. 마틴

단일 책임 원칙의 목표
- 클래스가 변경됐을 때 영향을 받는 액터가 하나여야 합니다.
- 클래스를 변경할 이유는 유일한 액터의 요구사항이 변경될 때로 제한되어야 합니다.
```
<br>

개방 폐쇄 원칙(OCP: Open-Closed Principle)
```text
클래스의 동작을 수정하지 않고 확장할 수 있어야 합니다.
- 로버트 C. 마틴
```
<br>

리스코프 치환 원칙(LSP: Liskov Subsitution Principle
```text
파생 클래스는 기본 클래스를 대체할 수 있어야 합니다.
- 로버트 C. 마틴
```
<br>

인터페이스 분리 원칙(ISP: Interface Segregation Principle)
```text
클라이언트별로 세분화된 인터페이스를 만드세요.
- 로버트 C. 마틴
```
<br>

의존성 역전 원칙(DIP: Dependency Inversion Principle)
```text
구체화가 아닌 추상화에 의존해야 합니다.
- 로버트 C. 마틴

첫째, 상위 모듈은 하위 모듈에 의존해서는 안된다. 상위 모듈과 하위 모듈 모두 추상화에 의존해야한다.
둘째, 추상화는 세부 사항에 의존해서는 안된다. 세부 사항이 추상화에 의존해야 한다.
```
<br>

## 지양 사항

### 0. 순환 참조
순환 참조는 두 개 이상의 객체나 컴포넌트가 서로를 참조함으로써 의존 관계에 사이클이 생기는 상황을 말합니다. 예를 들어, 객체 A가 객체 B를 참조하고, 객체 B가 객체 A를 참조하는 양방향 참조는 대표적인 순환 참조의 예입니다. 그리고 이러한 순환 참조는 소프트웨어 설계에서 자주 볼 수 있는 **대표적인 안티패턴 중 하나**입니다.
```java
@Data
@NoArgsConstructor
@Entity(name = "team")
class TeamJpaEntity {
	@Id
	private String id;

	@Column
	private String name;

	@OneToMany(mappedBy = "myTeam")
	private List<MemberJpaEntity> members;
}

@Data
@NoArgsConstructor
@Entity(name = "member")
class MemberJpaEntity {
	@Id
	private String id;

	@Column
	private String name;

	@ManyToOne
	@JoinColumn(name = "my_team_id")
	private TeamJpaEntity myTeam;
}
```
Team과 Member 클래스에 JPA의 @Entity 어노테이션을 적용하고 @OneToMany와 @ManyToONE 어노테이션을 이용해 양방향 연관관계 매핑을 적용했습니다.
<br>
### JPA의 양방향 매핑은 순환 참조입니다.
순환 참조는 소프트웨어 설계에서 피해야 하는 잘 알려진 대표적인 안티패턴입니다. 순환 참조가 발생하다는 것은 서로에게 강하게 의존한다는 의미입니다. 사실상 하나의 컴포넌트라는 의미이며, 책임이 제대로 구분돼 있지 않다는 의미입니다. 따라서 순환 참조가 있는 컴포턴트는 SOLD하지도 않습니다.
<br>

### 1. 스마트 UI 패턴
스마트 UI 패턴은 에릭 에반스(ERic Evans)의 <<도메인 주도 설계>>에서 소개 돼 유명해진 안티패턴입니다. 에릭 에반스가 말하는 스마트 UI(User Interface: 유저 인터페이스)는 다음과 같은 특징을 가진 코드를 말합니다.
```text
1. 스마트 UI는 데이터 입출력을 UI 레벨에서 처리합니다.

2. 스마트 UI는 비즈니스 로직도 UI 레벨에서 처리합니다.

3. 스마트 UI는 데이터베이스와 통신하는 코드도 UI 레벨에서 처리합니다.
```
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cafe/{cafeId}/boards/{boardId}/posts")
public class PostController {
	private final CafeMemberJpaRepository cafeMemberJpaRepository;
	private final BoardJpaRepository boardJpaRepository;
	private final PostJpaRepository postJpaRepository;
	private final PostRender postRender;
	
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasRole('USER')")
	public Success<PostViewModel> create(
		@PathVariable long cafeId,
		@PathVariable long boardId,
		@RequestBody @Valid PostCreateRequest postCreateRequest,
		@Injected UserPrincipal userPrincipal) {
		long userId = userPrincipal.getId();
		long currentTimestamp = Instant.now().toEpochMilli();
		
		// 카페의 회원인지 검증
		CafeMember cafeMember = cafeMemberJpaRepository.findByCafeIdAndUserId(cafeId, userId)
			.orElseThrow(() -> new ForbiddenAccessException());
		User user = userJpaRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException());
		Cafe cafe = cafeMember.getCafe();
		Borad board = boardJpaRepository.findById(boardId)
			.orElseThrow(() -> new BoardNotFoundException());
		Post post = new Post();
		post.setTitle(postCreateRequest.getTitle());
		post.setContent(postCreateRequest.getContent());
		post.setCafe(cafe);
		post.setBoard(board);
		
		// API 호출자를 게시물의 작성자로 입력
		post.setWriter(user);
		
		// 게시물의 작성 시간에 현재 시각을 입력
		post.setCreatedTimestamp(currentTimestamp);
		post.setModifiedTimestamp(currentTimestamp);
		post = postJpaRepository.save(post);
		
		// 카페의 가장 최신 게시물 시간을 현재 시간으로 변경
		cafe.setNewPostTimestamp(currentTimestamp);
		cafe = cafeJpaRepository.save(post);
		return Success.create(postRender.render(post));
	}
}
```
이러한 로직은 비즈니스 로직입니다. 그래서 이러한 로직이 UI 수준인 컨트롤러 컴포넌트에 위치해서는 안됩니다. 모든 코드가 오롯이 기능이 동작하는 데만 초점을 맞춰 작성합니다. 그러한 탓에 사실상 모든 API는 어떤 스크립트를 실행하고 응답하는 수준에 그치기 때문에 **확장성이 떨어지고 유지보수성**도 떨어집니다.

### 2. 양방향 레이어드 아키텍처
양방향 레이어드 아키텍처는 레이어드 아키텍처를 지향해 개발했지만, 레이어드 아키텍처가 반드시 지켜야 할 가장 기초적인 제약을 위반할 때를 지칭하는 말입니다. 그리고 여기서 가장 기초적인 제약이란 **'레이어 간 의존 방향은 단방향을 유지해야 한다'** 라는 것입니다. <br>
<br>
다음은 하위 레이어인 서비스 계층에서 상위 레이어의 모델에 접근하는 대표적인 사례입니다.
```java
@Service
@RequiredArgsConstructor
public class PostService {
	private final CafeMemberJpaRepository cafeMemberJpaRepository;
	private final BoardJpaRepository boardJpaRepository;
	private final PostJpaRepository postJpaRepository;

	@Transactional
	public Post create(
		long cafeId,
		long boardId,
		long writerId
		PostCreateRequest postCreateRequest) { // API 요청을 받는 모델인데 비즈니스 레이에서 사용함
		long userId = userPrincipal.getId();
		long currentTimestamp = Instant.now().toEpochMilli();
		
		// 카페의 회원인지 검증
		CafeMember cafeMember = cafeMemberJpaRepository.findByCafeIdAndUserId(cafeId, userId)
			.orElseThrow(() -> new ForbiddenAccessException());
		User user = userJpaRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException());
		Cafe cafe = cafeMember.getCafe();
		Borad board = boardJpaRepository.findById(boardId)
			.orElseThrow(() -> new BoardNotFoundException());
		Post post = new Post();
		post.setTitle(postCreateRequest.getTitle());
		post.setContent(postCreateRequest.getContent());
		post.setCafe(cafe);
		post.setBoard(board);
		post.setWriter(user);
		post.setCreatedTimestamp(currentTimestamp);
		post.setModifiedTimestamp(currentTimestamp);
		post = postJpaRepository.save(post);
		
		cafe.setNewPostTimestamp(currentTimestamp);
		cafe = cafeJpaRepository.save(post);
		return post;
	}
}
```
PostCreateRequest 클래스는 API 레이어의 모델입니다. API로 들어오는 요청을 @RequestBody 어노테이션을 이용해 매핑하려고 만든 객체인데, 하위 레이어에 존재하는 서비스 컴포넌트로 전달해 서비스에서 이를 사용하고 있는 상황입니다. 비즈니스 레이어에 위치한 서비스 컴포넌트가 프레젠테이션 레이어에 위치한 객체에 의존하는 바람에 두 레이어간 양방향 의존관계가 생겼습니다. 이것은 좋게 말해 양방향 의존이지 실은 **순환 참조가 생겼다** 는 말과같습니다.

### 3. 트랜잭션 스크립트
트랜잭션 스크립트는 비즈니스 레이어에 위치하는 서비스 컴포넌트에서 발생하는 안티패턴입니다.
```java
@Service
@RequiredArgsConstructor
public class PostService {
	private final CafeMemberJpaRepository cafeMemberJpaRepository;
	private final BoardJpaRepository boardJpaRepository;
	private final PostJpaRepository postJpaRepository;

	@Transactional
	public Post create(
		long cafeId,
		long boardId,
		long writerId
		PostCreateCommnand postCreateCommand) { // 양방향 레이어드 의존성 제거
		long userId = userPrincipal.getId();
		long currentTimestamp = Instant.now().toEpochMilli();
		
		// 카페의 회원인지 검증
		CafeMember cafeMember = cafeMemberJpaRepository.findByCafeIdAndUserId(cafeId, userId)
			.orElseThrow(() -> new ForbiddenAccessException());
		User user = userJpaRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException());
		Cafe cafe = cafeMember.getCafe();
		Borad board = boardJpaRepository.findById(boardId)
			.orElseThrow(() -> new BoardNotFoundException());
		Post post = new Post();
		post.setTitle(postCreateRequest.getTitle());
		post.setContent(postCreateRequest.getContent());
		post.setCafe(cafe);
		post.setBoard(board);
		post.setWriter(user);
		post.setCreatedTimestamp(currentTimestamp);
		post.setModifiedTimestamp(currentTimestamp);
		post = postJpaRepository.save(post);
		
		cafe.setNewPostTimestamp(currentTimestamp);
		cafe = cafeJpaRepository.save(post);
		return post;
	}
}
```
서비스 컴포넌트의 동작이 사실상 트랜잭션이 걸려 있는 거대한 스크립트를 실행하는 것처럼 보입니다. 나아가 이러한 코드는 **객체지향보다 절차지향에 가까운 사례**이기 때문에 절차지향의 문제점을 그대로 가집니다. 변경에 취약하고 확장에 취약하며 업무가 병렬 처리되기 어렵습니다.

## Architecture Schematic Diagram
![image](https://github.com/WhereAreYouPJ/oop-architecture/assets/103410386/7fcf1b86-6fad-46a5-8fcf-58386005f91d)
