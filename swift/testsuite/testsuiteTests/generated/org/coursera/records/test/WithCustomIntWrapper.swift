import Foundation
import SwiftyJSON

struct WithCustomIntWrapper {
    
    let wrapper: Int?
    
    init(wrapper: Int?) {
        
        self.wrapper = wrapper
    }
    
    static func read(json: JSON) -> WithCustomIntWrapper {
        return WithCustomIntWrapper(
        wrapper:
        json["wrapper"].int
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let wrapper = self.wrapper {
            json["wrapper"] = JSON(wrapper)
        }
        
        return json
    }
}
