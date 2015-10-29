import Foundation
import SwiftyJSON

struct WithDateTime {
    
    let time: Int?
    
    init(time: Int?) {
        
        self.time = time
    }
    
    static func read(json: JSON) -> WithDateTime {
        return WithDateTime(
        time:
        json["time"].int
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let time = self.time {
            json["time"] = JSON(time)
        }
        
        return json
    }
}
