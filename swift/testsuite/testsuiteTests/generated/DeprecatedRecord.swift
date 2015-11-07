import Foundation
import SwiftyJSON

@available(*, deprecated, message="Use XYZ instead")
public struct DeprecatedRecord: Serializable {
    
    @available(*, deprecated)
    public let field1: String?
    
    @available(*, deprecated, message="Use XYZ instead")
    public let field2: String?
    
    public init(
        field1: String?,
        field2: String?
    ) {
        self.field1 = field1
        self.field2 = field2
    }
    
    public static func readJSON(json: JSON) throws -> DeprecatedRecord {
        return DeprecatedRecord(
            field1: try json["field1"].optional(.String).string,
            field2: try json["field2"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let field1 = self.field1 {
            dict["field1"] = field1
        }
        if let field2 = self.field2 {
            dict["field2"] = field2
        }
        return dict
    }
}
