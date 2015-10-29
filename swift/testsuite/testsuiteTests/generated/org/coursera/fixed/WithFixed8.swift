import Foundation
import SwiftyJSON

struct WithFixed8: JSONSerializable {
    
    let fixed: String?
    
    init(
        fixed: String?
    ) {
        self.fixed = fixed
    }
    
    static func read(json: JSON) -> WithFixed8 {
        return WithFixed8(
            fixed: json["fixed"].string
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let fixed = self.fixed {
            json["fixed"] = JSON(fixed)
        }
        return JSON(json)
    }
}
