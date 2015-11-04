import Foundation
import SwiftyJSON

public struct Message: JSONSerializable, Equatable {
    
    public let title: String?
    
    public let body: String?
    
    public init(
        title: String?,
        body: String?
    ) {
        self.title = title
        self.body = body
    }
    
    public static func read(json: JSON) -> Message {
        return Message(
            title: json["title"].string,
            body: json["body"].string
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let title = self.title {
            json["title"] = JSON(title)
        }
        if let body = self.body {
            json["body"] = JSON(body)
        }
        return JSON(json)
    }
}
public func ==(lhs: Message, rhs: Message) -> Bool {
    return (
        (lhs.title == nil ? (rhs.title == nil) : lhs.title! == rhs.title!) &&
        (lhs.body == nil ? (rhs.body == nil) : lhs.body! == rhs.body!) &&
        true
    )
}
