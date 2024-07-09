# Architecture-Guide

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

### 1. 양방향 매핑

### 2. 스마트 UI 패턴

### 3. 양방향 레이어드 아키텍처

### 4. 트랜잭션 스크립트
