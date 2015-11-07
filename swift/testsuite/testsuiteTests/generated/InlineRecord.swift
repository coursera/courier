import Foundation
import SwiftyJSON

public struct InlineRecord: Serializable {
    
    public let value: Int?
    
    public init(
        value: Int?
    ) {
        self.value = value
    }
    
    public static func readJSON(json: JSON) throws -> InlineRecord {
        return InlineRecord(
            value: try json["value"].optional(.Number).int
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
