import UIKit
import DesignKit

final class TypingBubble: UIView {
    
    // MARK: - Private Properties
    
    private var dots: [UIView] = []
    private var displayLink: CADisplayLink?
    private var animationStartTime: CFTimeInterval = 0
    
    // MARK: - Initialization
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupDots()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Public Methods
    
    func startAnimating() {
        stopAnimating()
        animationStartTime = CACurrentMediaTime()
        displayLink = CADisplayLink(target: self, selector: #selector(updateAnimation))
        displayLink?.add(to: .main, forMode: .common)
    }
    
    func stopAnimating() {
        displayLink?.invalidate()
        displayLink = nil
        dots.forEach { $0.transform = .identity }
    }
    
    // MARK: - Private Methods
    
    private func setupDots() {
        for _ in 0..<3 {
            let dot = UIView()
            dot.backgroundColor = ColorName.contentSoft600.color
            dot.layer.cornerRadius = 3
            dot.translatesAutoresizingMaskIntoConstraints = false
            addSubview(dot)
            dots.append(dot)
        }
        
        let dotWidth: CGFloat = 6
        let spacing: CGFloat = 4
        
        NSLayoutConstraint.activate(dots.enumerated().flatMap { index, dot -> [NSLayoutConstraint] in
            let leadingConstant = CGFloat(index) * (dotWidth + spacing)
            return [
                dot.widthAnchor.constraint(equalToConstant: dotWidth),
                dot.heightAnchor.constraint(equalToConstant: dotWidth),
                dot.leadingAnchor.constraint(equalTo: leadingAnchor, constant: leadingConstant),
                dot.centerYAnchor.constraint(equalTo: centerYAnchor)
            ]
        })
    }
    
    @objc private func updateAnimation() {
        let duration: CFTimeInterval = 1.0
        let currentTime = CACurrentMediaTime() - animationStartTime
        
        for (index, dot) in dots.enumerated() {
            let delay = Double(index) * 0.2
            let time = currentTime - delay
            let normalizedTime = (time.truncatingRemainder(dividingBy: duration)) / duration
            
            if normalizedTime < 0.5 {
                let scale = 1.0 + sin(normalizedTime * .pi) * 0.3
                dot.transform = CGAffineTransform(scaleX: scale, y: scale)
            } else {
                dot.transform = .identity
            }
        }
    }
} 