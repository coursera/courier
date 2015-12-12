import Foundation
import SwiftyJSON

public enum EnumProperties: Equatable, Hashable {
    
    case APPLE
    
    case ORANGE
    
    case BANANA
    case UNKNOWN$(String)
    private struct Strings {
        static let APPLE = "APPLE"
        static let ORANGE = "ORANGE"
        static let BANANA = "BANANA"
    }
    public static func read(symbol: String) -> EnumProperties {
        switch symbol {
        case EnumProperties.Strings.APPLE:
            return .APPLE
        case EnumProperties.Strings.ORANGE:
            return .ORANGE
        case EnumProperties.Strings.BANANA:
            return .BANANA
        default:
            return .UNKNOWN$(symbol)
        }
    }
    public func write() -> String {
        switch self {
        case .APPLE:
            return EnumProperties.Strings.APPLE
        case .ORANGE:
            return EnumProperties.Strings.ORANGE
        case .BANANA:
            return EnumProperties.Strings.BANANA
        case .UNKNOWN$(let symbol):
            return symbol
        }
    }
    public var hashValue: Int {
        return write().hashValue
    }
}
public func ==(lhs: EnumProperties, rhs: EnumProperties) -> Bool {
    switch (lhs, rhs) {
    case (.APPLE, .APPLE), (.ORANGE, .ORANGE), (.BANANA, .BANANA):
        return true
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return lhs == rhs
    default:
        return false
    }
}
