import Foundation
import SwiftyJSON

public enum Fruits: Equatable, Hashable {
    
    /**
        An Apple.
    */
    case APPLE
    
    case BANANA
    
    case ORANGE
    
    @available(*, deprecated, message="No more pineapples.")
    case PINEAPPLE
    case UNKNOWN$(String)
    private struct Strings {
        static let APPLE = "APPLE"
        static let BANANA = "BANANA"
        static let ORANGE = "ORANGE"
        static let PINEAPPLE = "PINEAPPLE"
    }
    public static func read(symbol: String) -> Fruits {
        switch symbol {
        case Fruits.Strings.APPLE:
            return .APPLE
        case Fruits.Strings.BANANA:
            return .BANANA
        case Fruits.Strings.ORANGE:
            return .ORANGE
        case Fruits.Strings.PINEAPPLE:
            return .PINEAPPLE
        default:
            return .UNKNOWN$(symbol)
        }
    }
    public func write() -> String {
        switch self {
        case .APPLE:
            return Fruits.Strings.APPLE
        case .BANANA:
            return Fruits.Strings.BANANA
        case .ORANGE:
            return Fruits.Strings.ORANGE
        case .PINEAPPLE:
            return Fruits.Strings.PINEAPPLE
        case .UNKNOWN$(let symbol):
            return symbol
        }
    }
    public var hashValue: Int {
        return write().hashValue
    }
}
public func ==(lhs: Fruits, rhs: Fruits) -> Bool {
    switch (lhs, rhs) {
    case (.APPLE, .APPLE), (.BANANA, .BANANA), (.ORANGE, .ORANGE), (.PINEAPPLE, .PINEAPPLE):
        return true
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return lhs == rhs
    default:
        return false
    }
}
