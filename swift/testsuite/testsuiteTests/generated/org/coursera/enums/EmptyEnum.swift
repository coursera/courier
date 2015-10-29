import Foundation
import SwiftyJSON

enum EmptyEnum: Equatable, Hashable {
    case UNKNOWN$(String)
    private struct Strings {
    }
    static func read(symbol: String) -> EmptyEnum {
        switch symbol {
        default:
            return .UNKNOWN$(symbol)
        }
    }
    func write() -> String {
        switch self {
        case .UNKNOWN$(let symbol):
            return symbol
        }
    }
    var hashValue: Int {
        return write().hashValue
    }
}
func ==(lhs: EmptyEnum, rhs: EmptyEnum) -> Bool {
    switch (lhs, rhs) {
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return lhs == rhs
    }
}
