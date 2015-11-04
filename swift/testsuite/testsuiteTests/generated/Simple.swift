import Foundation
import SwiftyJSON

/**
    A simple record
*/
public struct Simple: JSONSerializable, Equatable {
    
    /**
        A simple field
    */
    public let message: String?
    
    public init(
        message: String?
    ) {
        self.message = message
    }
    
    public static func read(json: JSON) -> Simple {
        return Simple(
            message: json["message"].string
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let message = self.message {
            json["message"] = JSON(message)
        }
        return JSON(json)
    }
}
public func ==(lhs: Simple, rhs: Simple) -> Bool {
    return (
        (lhs.message == nil ? (rhs.message == nil) : lhs.message! == rhs.message!) &&
        true
    )
}
