import Foundation
import SwiftyJSON

public struct Message: Serializable, Equatable {
    
    public let title: String?
    
    public let body: String?
    
    public init(
        title: String?,
        body: String?
    ) {
        self.title = title
        self.body = body
    }
    
    public static func readJSON(json: JSON) throws -> Message {
        return Message(
            title: json["title"].string,
            body: json["body"].string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let title = self.title {
            dict["title"] = title
        }
        if let body = self.body {
            dict["body"] = body
        }
        return dict
    }
}
public func ==(lhs: Message, rhs: Message) -> Bool {
    return (
        (lhs.title == nil ? (rhs.title == nil) : lhs.title! == rhs.title!) &&
        (lhs.body == nil ? (rhs.body == nil) : lhs.body! == rhs.body!) &&
        true
    )
}
