import Foundation

public struct Coercers {
  public struct CustomInt {
    let int: Int
  }

  public struct CustomIntCoercer {
    public static read(value: String) -> CustomInt {
      CustomInt(Int(value))
    }

    public static write(value: CustomInt) -> String {
      String(value.int)
    }
  }
}
