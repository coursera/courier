import Foundation
import SwiftyJSON

enum Fruits: Equatable, Hashable {
    
    /**
     * An Apple.
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
    static func read(symbol: String) -> Fruits {
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
    func write() -> String {
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
    var hashValue: Int {
        return write().hashValue
    }
}
func ==(lhs: Fruits, rhs: Fruits) -> Bool {
    switch (lhs, rhs) {
    case (.APPLE, .APPLE):
        return true
    case (.BANANA, .BANANA):
        return true
    case (.ORANGE, .ORANGE):
        return true
    case (.PINEAPPLE, .PINEAPPLE):
        return true
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return lhs == rhs
    default:
        return false
    }
}
