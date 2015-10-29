import Foundation
import SwiftyJSON

/**
    A simple record
*/
struct Simple: JSONSerializable, Equatable {
    
    /**
        A simple field
    */
    let message: String?
    
    init(
        message: String?
    ) {
        self.message = message
    }
    
    static func read(json: JSON) -> Simple {
        return Simple(
            message: json["message"].string
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let message = self.message {
            json["message"] = JSON(message)
        }
        return JSON(json)
    }
}
func ==(lhs: Simple, rhs: Simple) -> Bool {
    return (
        (lhs.message == nil ? (rhs.message == nil) : lhs.message! == rhs.message!) &&
        true
    )
}
