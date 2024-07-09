## Service Layer (Feat. Domain)

### 비즈니스 로직의 역할
```text
비즈니스 로직은 어디에 위치해야 할까요?
```
**'비즈니스 로직은 도메인 모델에 위치해야 한다'** 입니다. 도메인 모델이란 Cafe, Post, Board, User와 같은 객체들을 말합니다. 즉, 비즈니스 로직이 처리되는 '주(main)' 영역은 도메인 모델이어야 합니다. 서비스는 도메인을 불러와서 도메인에 일을 시키는 정도의 역할만 해야 합니다.
<br><br>

**개발자 A의 비즈니스 로직**
![image](https://github.com/WhereAreYouPJ/oop-architecture/assets/103410386/ab9140b9-eb6d-48a9-94b1-4d9ace092bae)
```text
1. 레포지터리에서 데이터를 불러온다

2. 데이터를 보고 비즈니스 로직을 처리한다.

3. 레포지터리에 데이터를 저장한다.

4. 컨트롤러에 응답한다.
```
이는 어플리케이션이 전혀 객체지향적이지 않습니다. 이러한 개념 모델에서 객체지향을 활용하기란 어렵습니다. 서비스의 로직은 점점 길어지고 비대해져서 서비스가 뚱뚱해지고(fat) 어플리케이션은 트랜잭션 스크립트에 가까워집니다.
<br><br>
디자인 패턴이나 SOLID를 논하기 전에 능동적인 도메인을 만드는 것이 먼저입니다. 그리고 나서 도메인끼리 협력하게 만들어야합니다.
<br><br>

**개발자  B의 비즈니스 로직**
![image](https://github.com/WhereAreYouPJ/oop-architecture/assets/103410386/63338517-997a-47f4-9cb4-f81412db5b06)
```text
1. 레포지터리에서 도메인 객체를 불러온다.

2. 도메인 객체에 일을 시킨다.

3. 레포지터리에 도메인 객체를 저장한다.

4. 컨트롤러에 응답한다.
```
이 미묘한 차이가 어플리케이션의 전체 품질에 큰 차이를 만듭니다. 절차지향이었던 코드를 객체지향을 만듭니다. 서비스가 서비스의 역할을 하게 만들며, 도메인 모델이 서로 협력하게 만듭니다.
<br><br>
**서비스는 도메인 객체나 도메인 서비스라고 불리는 도메인에 일을 위임하는 공간이어야 합니다.**
<br><br>