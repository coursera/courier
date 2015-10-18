import Foundation
import SwiftyJSON

struct WithUnion: Equatable {
    
    let value: Union?
    
    init(value: Union?) {
        
        self.value = value
    }
    
    static func read(json: JSON) -> WithUnion {
        return WithUnion(
        value: json["value"].json.map { Union.read($0) })
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let value = self.value {
            json["value"] = JSON(value.write())
        }
        
        return json
    }
}
func ==(lhs: WithUnion, rhs: WithUnion) -> Bool {
    return (
    
    (lhs.value == nil ? (rhs.value == nil) : lhs.value! == rhs.value!) &&
    true)
}
