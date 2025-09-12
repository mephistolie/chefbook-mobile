import SwiftUI
import ChefBookMPP

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        DummyKt.MainUIViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}


struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea(.all)
    }
}
