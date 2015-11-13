import Foundation

public struct CustomInt: Equatable {
  let int: Int
}

public func ==(lhs: CustomInt, rhs: CustomInt) -> Bool {
    return lhs.int == rhs.int
}

public struct CustomIntCoercer: Coercer {
    public typealias CustomType = CustomInt
    public typealias DirectType = Int
    
    public static func coerceInput(value: Int) throws -> CustomInt {
        return CustomInt(int: value)
    }

    public static func coerceOutput(value: CustomInt) -> Int {
        return value.int
    }
}
