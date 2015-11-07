import Foundation
import SwiftyJSON

public struct WithFixed8: Serializable {
    
    public let fixed: String?
    
    public init(
        fixed: String?
    ) {
        self.fixed = fixed
    }
    
    public static func readJSON(json: JSON) throws -> WithFixed8 {
        return WithFixed8(
            fixed: try json["fixed"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let fixed = self.fixed {
            dict["fixed"] = fixed
        }
        return dict
    }
}
