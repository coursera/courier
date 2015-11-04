import Foundation
import SwiftyJSON

enum Fruits {
    
    case APPLE
    case BANANA
    case ORANGE
    case PINEAPPLE
    case UNKNOWN$(String)
    
    private static let APPLE$string = " APPLE"
    private static let BANANA$string = " BANANA"
    private static let ORANGE$string = " ORANGE"
    private static let PINEAPPLE$string = " PINEAPPLE"
    
    static func read(json: JSON) -> Fruits {
        let symbol$ = json.stringValue
        switch symbol$ {
        case APPLE$string:
            return .APPLE
        case BANANA$string:
            return .BANANA
        case ORANGE$string:
            return .ORANGE
        case PINEAPPLE$string:
            return .PINEAPPLE
        default:
            return .UNKNOWN$(symbol$)
        }
    }
    func write() -> JSON {
        switch self {
        case .APPLE:
            return JSON(Fruits.APPLE$string)
        case .BANANA:
            return JSON(Fruits.BANANA$string)
        case .ORANGE:
            return JSON(Fruits.ORANGE$string)
        case .PINEAPPLE:
            return JSON(Fruits.PINEAPPLE$string)
        case .UNKNOWN$(let symbol$):
            return JSON(symbol$)
        }
    }
}
