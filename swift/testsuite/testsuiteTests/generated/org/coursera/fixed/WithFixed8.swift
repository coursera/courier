import Foundation
import SwiftyJSON

struct WithFixed8 {
    
    let fixed: String?
    
    init(fixed: String?) {
        
        self.fixed = fixed
    }
    
    static func read(json: JSON) -> WithFixed8 {
        return WithFixed8(
        fixed: json["fixed"].string)
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let fixed = self.fixed {
            json["fixed"] = JSON(fixed)
        }
        
        return json
    }
}
