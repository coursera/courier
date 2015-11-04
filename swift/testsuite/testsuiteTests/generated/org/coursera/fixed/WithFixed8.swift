import Foundation
import SwiftyJSON

public struct WithFixed8: JSONSerializable {
    
    public let fixed: String?
    
    public init(
        fixed: String?
    ) {
        self.fixed = fixed
    }
    
    public static func read(json: JSON) -> WithFixed8 {
        return WithFixed8(
            fixed: json["fixed"].string
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let fixed = self.fixed {
            json["fixed"] = JSON(fixed)
        }
        return JSON(json)
    }
}
