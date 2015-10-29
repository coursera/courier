import Foundation
import SwiftyJSON

struct RecordKey {
    
    let x: Int?
    
    let y: Bool?
    
    init(x: Int?, y: Bool?) {
        
        self.x = x
        
        self.y = y
    }
    
    static func read(json: JSON) -> RecordKey {
        return RecordKey(
        x:
        json["x"].int,
        y:
        json["y"].bool
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let x = self.x {
            json["x"] = JSON(x)
        }
        
        if let y = self.y {
            json["y"] = JSON(y)
        }
        
        return json
    }
}
