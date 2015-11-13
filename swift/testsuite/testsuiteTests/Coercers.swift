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

public struct CustomIntWrapper: Equatable {
    let int: CustomInt
}

public func ==(lhs: CustomIntWrapper, rhs: CustomIntWrapper) -> Bool {
    return lhs.int == rhs.int
}

public struct CustomIntWrapperCoercer: Coercer {
    public typealias CustomType = CustomIntWrapper
    public typealias DirectType = Int
    
    public static func coerceInput(value: Int) throws -> CustomIntWrapper {
        return CustomIntWrapper(int: CustomInt(int: value))
    }
    
    public static func coerceOutput(value: CustomIntWrapper) -> Int {
        return value.int.int
    }
}

public struct NSDateCoercer: Coercer {
    public typealias CustomType = NSDate
    public typealias DirectType = Int
    
    public static func coerceInput(value: Int) throws -> NSDate {
        return NSDate(timeIntervalSince1970: Double(value * 1000))
    }
    
    public static func coerceOutput(value: NSDate) -> Int {
        return Int(value.timeIntervalSince1970) / 1000
    }
}