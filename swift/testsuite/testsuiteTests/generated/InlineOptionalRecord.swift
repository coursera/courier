import Foundation
import SwiftyJSON

public struct InlineOptionalRecord: Serializable {
    
    public let value: String?
    
    public init(
        value: String?
    ) {
        self.value = value
    }
    
    public static func readJSON(json: JSON) throws -> InlineOptionalRecord {
        return InlineOptionalRecord(
            value: try json["value"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let value = self.value {
            dict["value"] = value
        }
        return dict
    }
}
