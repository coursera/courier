import Foundation
import SwiftyJSON

public struct InlineRecord: JSONSerializable {
    
    public let value: Int?
    
    public init(
        value: Int?
    ) {
        self.value = value
    }
    
    public static func read(json: JSON) -> InlineRecord {
        return InlineRecord(
            value: json["value"].int
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let value = self.value {
            json["value"] = JSON(value)
        }
        return JSON(json)
    }
}
