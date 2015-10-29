import Foundation
import SwiftyJSON

struct InlineOptionalRecord: JSONSerializable {
    
    let value: String?
    
    init(
        value: String?
    ) {
        self.value = value
    }
    
    static func read(json: JSON) -> InlineOptionalRecord {
        return InlineOptionalRecord(
            value: json["value"].string
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let value = self.value {
            json["value"] = JSON(value)
        }
        return JSON(json)
    }
}
