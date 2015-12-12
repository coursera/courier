import Foundation
import SwiftyJSON

public struct ReservedClassFieldEscaping: Serializable {
    
    public let data: String?
    
    public let schema: String?
    
    public let copy: String?
    
    public let clone: String?
    
    public init(
        data: String?,
        schema: String?,
        copy: String?,
        clone: String?
    ) {
        self.data = data
        self.schema = schema
        self.copy = copy
        self.clone = clone
    }
    
    public static func readJSON(json: JSON) throws -> ReservedClassFieldEscaping {
        return ReservedClassFieldEscaping(
            data: try json["data"].optional(.String).string,
            schema: try json["schema"].optional(.String).string,
            copy: try json["copy"].optional(.String).string,
            clone: try json["clone"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let data = self.data {
            dict["data"] = data
        }
        if let schema = self.schema {
            dict["schema"] = schema
        }
        if let copy = self.copy {
            dict["copy"] = copy
        }
        if let clone = self.clone {
            dict["clone"] = clone
        }
        return dict
    }
}
