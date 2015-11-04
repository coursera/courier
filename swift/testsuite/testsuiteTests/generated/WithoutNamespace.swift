import Foundation
import SwiftyJSON

public struct WithoutNamespace: JSONSerializable {
    
    public let field1: String?
    
    public init(
        field1: String?
    ) {
        self.field1 = field1
    }
    
    public static func read(json: JSON) -> WithoutNamespace {
        return WithoutNamespace(
            field1: json["field1"].string
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let field1 = self.field1 {
            json["field1"] = JSON(field1)
        }
        return JSON(json)
    }
}
