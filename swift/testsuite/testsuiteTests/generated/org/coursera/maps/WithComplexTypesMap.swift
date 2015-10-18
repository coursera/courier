import Foundation
import SwiftyJSON

struct WithComplexTypesMap: Equatable {
    
    let empties: [String: Empty]?
    
    let fruits: [String: Fruits]?
    
    let arrays: [String: [Simple]]?
    
    let maps: [String: [String: Simple]]?
    
    let unions: [String: WithComplexTypesMapUnion]?
    
    let fixed: [String: String]?
    
    init(empties: [String: Empty]?, fruits: [String: Fruits]?, arrays: [String: [Simple]]?, maps: [String: [String: Simple]]?, unions: [String: WithComplexTypesMapUnion]?, fixed: [String: String]?) {
        
        self.empties = empties
        
        self.fruits = fruits
        
        self.arrays = arrays
        
        self.maps = maps
        
        self.unions = unions
        
        self.fixed = fixed
    }
    
    static func read(json: JSON) -> WithComplexTypesMap {
        return WithComplexTypesMap(
        empties: json["empties"].dictionary.map { $0.mapValues { Empty.read($0.jsonValue) } },
        fruits: json["fruits"].dictionary.map { $0.mapValues { Fruits.read($0.stringValue) } },
        arrays: json["arrays"].dictionary.map { $0.mapValues { $0.arrayValue.map { Simple.read($0.jsonValue) } } },
        maps: json["maps"].dictionary.map { $0.mapValues { $0.dictionaryValue.mapValues { Simple.read($0.jsonValue) } } },
        unions: json["unions"].dictionary.map { $0.mapValues { WithComplexTypesMapUnion.read($0.jsonValue) } },
        fixed: json["fixed"].dictionary.map { $0.mapValues { $0.stringValue } })
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let empties = self.empties {
            json["empties"] = JSON(empties.mapValues { JSON($0.write()) })
        }
        if let fruits = self.fruits {
            json["fruits"] = JSON(fruits.mapValues { JSON($0.write()) })
        }
        if let arrays = self.arrays {
            json["arrays"] = JSON(arrays.mapValues { JSON($0.map { JSON($0.write()) }) })
        }
        if let maps = self.maps {
            json["maps"] = JSON(maps.mapValues { JSON($0.mapValues { JSON($0.write()) }) })
        }
        if let unions = self.unions {
            json["unions"] = JSON(unions.mapValues { JSON($0.write()) })
        }
        if let fixed = self.fixed {
            json["fixed"] = JSON(fixed.mapValues { JSON($0) })
        }
        
        return json
    }
}
func ==(lhs: WithComplexTypesMap, rhs: WithComplexTypesMap) -> Bool {
    return (
    
    (lhs.empties == nil ? (rhs.empties == nil) : lhs.empties! == rhs.empties!) &&
    
    (lhs.fruits == nil ? (rhs.fruits == nil) : lhs.fruits! == rhs.fruits!) &&
    
    (lhs.arrays == nil ? (rhs.arrays == nil) : lhs.arrays! == rhs.arrays!) &&
    
    (lhs.maps == nil ? (rhs.maps == nil) : lhs.maps! == rhs.maps!) &&
    
    (lhs.unions == nil ? (rhs.unions == nil) : lhs.unions! == rhs.unions!) &&
    
    (lhs.fixed == nil ? (rhs.fixed == nil) : lhs.fixed! == rhs.fixed!) &&
    true)
}
