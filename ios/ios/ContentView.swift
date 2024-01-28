import SwiftUI
import common

struct ContentView: View {
	var body: some View {
        let t = Dummy()
        
        Text(t.test())
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
