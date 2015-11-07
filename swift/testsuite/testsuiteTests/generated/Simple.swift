import Foundation
import SwiftyJSON

/**
    A simple record
*/
public struct Simple: Serializable, Equatable {
    
    /**
        A simple field
    */
    public let message: String?
    
    public init(
        message: String?
    ) {
        self.message = message
    }
    
    public static func readJSON(json: JSON) throws -> Simple {
        return Simple(
            message: try json["message"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let message = self.message {
            dict["message"] = message
        }
        return dict
    }
}
public func ==(lhs: Simple, rhs: Simple) -> Bool {
    return (
        (lhs.message == nil ? (rhs.message == nil) : lhs.message! == rhs.message!) &&
        true
    )
}
