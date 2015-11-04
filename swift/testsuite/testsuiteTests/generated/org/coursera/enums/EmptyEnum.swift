import Foundation
import SwiftyJSON

public enum EmptyEnum: Equatable, Hashable {
    case UNKNOWN$(String)
    private struct Strings {
    }
    public static func read(symbol: String) -> EmptyEnum {
        switch symbol {
        default:
            return .UNKNOWN$(symbol)
        }
    }
    public func write() -> String {
        switch self {
        case .UNKNOWN$(let symbol):
            return symbol
        }
    }
    public var hashValue: Int {
        return write().hashValue
    }
}
public func ==(lhs: EmptyEnum, rhs: EmptyEnum) -> Bool {
    switch (lhs, rhs) {
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return lhs == rhs
    }
}
