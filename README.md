# kotlin-convenience-store-precourse

## 프로젝트 개요

편의점에서 사용자가 상품을 구매하고, 프로모션 및 멤버십 할인을 통해 최종 결제 금액을 계산하는 애플리케이션입니다.
재고 상황과 할인 혜택을 고려하여 결제 시스템을 구현하고, 영수증을 출력하여 사용자에게 안내합니다.

## 요구 사항

### 기능 요구 사항

1. 상품 선택 및 구매
    - 사용자가 구매할 상품의 이름과 수량을 입력받아 최종 결제 금액을 계산한다.
    - 입력 형식: [상품명-수량],[상품명-수량]
    - 예외 처리: 존재하지 않는 상품명이나 재고 수량을 초과할 경우 에러 메시지를 출력한다.

2. 상품 및 프로모션 정보 불러오기
    - 상품 목록과 프로모션 목록을 .md 파일에서 읽어오고, 사용할 수 있는 형태로 가공한다.

3. 재고 관리
    - 결제 완료 시 재고 수량에서 구매된 수량만큼 차감한다.
    - 재고 부족 시 구매할 수 없도록 안내한다.

4. 할인 정책
    - 프로모션 할인
        - 지정된 상품에 대해 일정 수량 구매 시 무료 증정 혜택을 제공한다. (ex: 1+1, 2+1)
        - 프로모션 재고가 있는 경우에만 할인을 적용한다.
    - 멤버십 할인
        - 프로모션 혜택이 적용된 후 남은 금액에 대해 멤버십 할인 (30%, 최대 한도 8,000원)을 적용한다.

5. 영수증 출력
    - 사용자 구매 내역, 할인 내역, 최종 결제 금액을 영수증 형태로 출력한다.
    - 영수증 항목: 구매 상품, 증정 상품, 총 금액, 행사할인, 멤버십 할인, 내실돈(최종 결제 금액)

6. 추가 구매 여부
    - 결제 후 추가 구매를 선택할 수 있다.
    - 구매가 완료되면 재고가 업데이트된 상품 목록을 출력한다.

### 입출력 요구 사항

- 입력
    - 상품 목록 및 프로모션 목록은 파일 입출력 (products.md, promotions.md)을 통해 불러온다.
    - 구매할 상품명과 수량을 입력받는다.
    - 프로모션 혜택 및 멤버십 할인 적용 여부에 대해 추가 입력을 받는다.

- 출력
    - 현재 보유 중인 상품 목록을 출력한다.
    - 구매한 상품의 내역과 할인 내역을 영수증 형태로 정리하여 출력한다.

- 예외 상황
    - 잘못된 입력 시 [ERROR]로 시작하는 메시지를 출력하고 올바른 형식으로 다시 입력받는다.

### 프로그래밍 요구 사항

- **언어 및 버전**: Kotlin 1.9.24에서 실행 가능해야 한다.
- **코드 컨벤션**: Kotlin Style Guide를 따른다.
- **코드 구조**:
    - 각 함수는 한 가지 일만 수행하도록 구현하며, 함수의 길이는 10라인을 넘지 않도록 설계한다.
    - 들여쓰기(indentation) depth는 최대 2까지만 허용한다. (e.g., while문 안에 if문이 있으면 들여쓰기는 2이다)
- **테스트**:
    - JUnit 5와 AssertJ를 사용하여 각 기능에 대한 단위 테스트를 작성해, 기능이 정상적으로 작동하는지 확인한다.
    - UI(System.out, System.in, Scanner) 관련 로직은 단위 테스트에서 제외한다.
- **프로젝트 구성**:
    - Java가 아닌 Kotlin 코드로만 구현한다.
    - 프로그램 실행의 시작점은 `Application`의 `main()`이어야 한다.
    - `build.gradle.kts` 파일은 변경할 수 없으며, 제공된 라이브러리 외의 외부 라이브러리는 사용하지 않는다.
    - `camp.nextstep.edu.missionutils`에서 제공하는 `DateTimes`와 `Console` API를 사용하여 구현한다.
        - 현재 날짜와 시간은 `DateTimes.now()`를 활용한다.
        - 사용자 입력은 `Console.readLine()`을 통해 받아 처리한다.
- **프로그램 종료**: `System.exit()` 또는 `exitProcess()`를 호출하지 않는다.
- **이름과 위치**: 별도로 명시되지 않은 한 파일, 패키지 등의 이름을 바꾸거나 이동하지 않는다.
- **else 사용 지양**:
    - 가능하면 `else`를 사용하지 않고 if 조건절에서 값을 return하는 방식으로 구현한다. (guard clause)
- **Enum 클래스 사용**: Enum 클래스를 적용하여 프로그램을 구현한다.
- **입출력 담당 클래스**:
    - InputView와 OutputView 클래스를 별도로 구현하여, 입출력 관련 로직을 분리한다.
    - 클래스 이름, 메소드 반환 유형, 시그니처 등은 자유롭게 수정할 수 있다.

  예시:
  ```kotlin
  class InputView {
      fun readItem(): String {
          println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])")
          return Console.readLine()
      }
      // ...
  }

  class OutputView {
      fun printProducts() {
          println("- 콜라 1,000원 10개 탄산2+1")
          // ...
      }
      // ...
  }

## 기능 구현 사항

### 1. 상품 선택 및 구매

- 상품명과 수량을 입력받아 파싱하고, 상품명과 수량을 추출하여 상품 정보를 조회합니다.
- 사용자가 구매하려는 상품이 존재하지 않거나 재고를 초과할 경우, 예외를 발생시키며 에러 메시지를 출력하고 올바른 입력을 다시 요청합니다.
- 입력 형식은 `[상품명-수량]`이며, 여러 상품을 입력할 경우 `[상품명-수량],[상품명-수량]` 형식으로 입력합니다.

### 2. 상품 및 프로모션 정보 불러오기

- 상품 목록과 프로모션 목록을 .md 파일에서 읽어오고, 사용할 수 있는 형태로 가공합니다.

### 3. 재고 관리

- 결제 후 구매된 수량만큼 상품의 재고 수량을 차감합니다.
- 프로모션 적용 시 해당 상품의 프로모션 재고도 관리하며, 프로모션 재고가 부족할 경우 일반 재고를 사용합니다.
- 구매 시 재고가 부족하면 에러 메시지를 출력하여 사용자에게 재고 부족을 안내합니다.

### 4. 할인 정책

- **프로모션 할인**:
    - 프로모션을 적용할 상품이 있는지 확인하고, 가능한 경우 할인 혜택을 제공합니다.
    - 프로모션의 종류에 따라 N+1 형태의 할인을 적용합니다.
    - 프로모션 재고가 부족할 경우 일부만 혜택을 적용할 수 있습니다.
    - 프로모션이 적용되지 않는 물품의 경우에는 일반 재고를 소진합니다.
        - ex) 2+1 행사 중인 콜라의 기본 재고:7, 프로모션 재고:7일 시에 소비자가 콜라 8개를 구매하면
          콜라의 프로모션 재고 6개, 기본 재고 2개를 소진함

- **멤버십 할인**:
    - 결제 총액에 대해 최대 8,000원까지 30% 멤버십 할인을 적용합니다.
    - 사용자가 멤버십 할인 여부를 Y/N으로 입력받으며, 잘못된 값이 입력될 경우 에러 메시지를 출력합니다.

### 5. 영수증 출력

- 최종 결제 내역을 포함한 영수증을 출력합니다.

```
==============W 편의점================
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
=============증	정===============
콜라		1
====================================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000
```

### 6. 추가 구매 여부 확인

- 결제 후 사용자에게 추가 구매 여부를 Y/N으로 입력받고, 입력에 따라 추가 구매를 반복할 수 있도록 합니다.
    - Y를 입력하면 재고가 업데이트된 상품 목록을 출력하고 새로운 상품을 구매할 수 있게 합니다.
    - N을 입력하면 프로그램을 종료합니다.

### 7. 예외 처리

- 잘못된 사용자 입력과 파일 입력에 대해 예외를 처리합니다.

## 예외 상황 정리

### 1. 상품 입력에 대한 예외 처리

| 예외 상황              | 예시 입력               | 동작    | 출력                                            |
|--------------------|---------------------|-------|-----------------------------------------------|
| 잘못된 입력 형식          | `[콜라]`, `콜라-2`      | 예외 발생 | `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`    |
| 존재하지 않는 상품을 입력한 경우 | `[존재하지않는상품-2]`      | 예외 발생 | `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`          |
| 재고를 초과한 수량을 입력한 경우 | `[콜라-11]`           | 예외 발생 | `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.` |
| 잘못된 수량(음수 또는 0) 입력 | `[콜라--1]`, `[콜라-0]` | 예외 발생 | `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`              |
| 빈 문자열을 입력한 경우      | `""`                | 예외 발생 | `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`              |

### 2. 사용자 동의 여부에 대한 입력의 예외 처리

| 예외 상황            | 예시 입력 | 동작    | 출력                               |
|------------------|-------|-------|----------------------------------|
| Y 또는 N 이외의 값을 입력 | `Z`   | 예외 발생 | `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.` |
