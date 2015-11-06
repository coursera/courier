import Foundation
import SwiftyJSON

public struct WithoutNamespace: Serializable {
    
    public let field1: String?
    
    public init(
        field1: String?
    ) {
        self.field1 = field1
    }
    
    public static func readJSON(json: JSON) throws -> WithoutNamespace {
        return WithoutNamespace(
            field1: json["field1"].string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let field1 = self.field1 {
            dict["field1"] = field1
        }
        return dict
    }
}
