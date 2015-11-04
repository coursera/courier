import Foundation
import SwiftyJSON

public struct InlineOptionalRecord: JSONSerializable {
    
    public let value: String?
    
    public init(
        value: String?
    ) {
        self.value = value
    }
    
    public static func read(json: JSON) -> InlineOptionalRecord {
        return InlineOptionalRecord(
            value: json["value"].string
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
