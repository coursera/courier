import Foundation
import SwiftyJSON

struct InlineOptionalRecord {
    
    let value: String?
    
    init(value: String?) {
        
        self.value = value
    }
    
    static func read(json: JSON) -> InlineOptionalRecord {
        return InlineOptionalRecord(
        value: json["value"].string)
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let value = self.value {
            json["value"] = JSON(value)
        }
        
        return json
    }
}
