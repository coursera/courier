import Foundation
import SwiftyJSON

struct Message: Equatable {
    
    let title: String?
    
    let body: String?
    
    init(title: String?, body: String?) {
        
        self.title = title
        
        self.body = body
    }
    
    static func read(json: JSON) -> Message {
        return Message(
        title: json["title"].string,
        body: json["body"].string)
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let title = self.title {
            json["title"] = JSON(title)
        }
        if let body = self.body {
            json["body"] = JSON(body)
        }
        
        return json
    }
}
func ==(lhs: Message, rhs: Message) -> Bool {
    return (
    
    (lhs.title == nil ? (rhs.title == nil) : lhs.title! == rhs.title!) &&
    
    (lhs.body == nil ? (rhs.body == nil) : lhs.body! == rhs.body!) &&
    true)
}
