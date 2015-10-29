import Foundation
import SwiftyJSON

struct WithCustomTypesMap: Equatable {
    
    let ints: [String: Int]?
    
    init(ints: [String: Int]?) {
        
        self.ints = ints
    }
    
    static func read(json: JSON) -> WithCustomTypesMap {
        return WithCustomTypesMap(
        ints:
        json["ints"].dictionary.map { $0.mapValues { $0.intValue } }
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let ints = self.ints {
            json["ints"] = JSON(ints)
        }
        
        return json
    }
}
func ==(lhs: WithCustomTypesMap, rhs: WithCustomTypesMap) -> Bool {
    return (
    
    (lhs.ints == nil ? (rhs.ints == nil) : lhs.ints! == rhs.ints!) &&
    true)
}
