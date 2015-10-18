import Foundation
import SwiftyJSON

struct InlineRecord {
    
    let value: Int?
    
    init(value: Int?) {
        
        self.value = value
    }
    
    static func read(json: JSON) -> InlineRecord {
        return InlineRecord(
        value: json["value"].int)
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let value = self.value {
            json["value"] = JSON(value)
        }
        
        return json
    }
}
