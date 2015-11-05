import Foundation
import SwiftyJSON

public struct WithFixed8: JSONSerializable, DataTreeSerializable {
    
    public let fixed: String?
    
    public init(
        fixed: String?
    ) {
        self.fixed = fixed
    }
    
    public static func readJSON(json: JSON) -> WithFixed8 {
        return WithFixed8(
            fixed: json["fixed"].string
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithFixed8 {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let fixed = self.fixed {
            dict["fixed"] = fixed
        }
        return dict
    }
}
