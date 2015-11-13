import Foundation
import SwiftyJSON

public struct WithDateTime: Serializable, Equatable {
    
    public let createdAt: NSDate?
    
    public init(
        createdAt: NSDate?
    ) {
        self.createdAt = createdAt
    }
    
    public static func readJSON(json: JSON) throws -> WithDateTime {
        return WithDateTime(
            createdAt: try json["createdAt"].optional(.Number).int.map { try NSDateCoercer.coerceInput($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let createdAt = self.createdAt {
            dict["createdAt"] = NSDateCoercer.coerceOutput(createdAt)
        }
        return dict
    }
}
public func ==(lhs: WithDateTime, rhs: WithDateTime) -> Bool {
    return (
        (lhs.createdAt == nil ? (rhs.createdAt == nil) : lhs.createdAt! == rhs.createdAt!) &&
        true
    )
}
