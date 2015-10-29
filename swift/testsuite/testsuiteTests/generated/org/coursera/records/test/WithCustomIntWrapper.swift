import Foundation
import SwiftyJSON

struct WithCustomIntWrapper: JSONSerializable {
    
    let wrapper: Int?
    
    init(
        wrapper: Int?
    ) {
        self.wrapper = wrapper
    }
    
    static func read(json: JSON) -> WithCustomIntWrapper {
        return WithCustomIntWrapper(
            wrapper: json["wrapper"].int
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let wrapper = self.wrapper {
            json["wrapper"] = JSON(wrapper)
        }
        return JSON(json)
    }
}
