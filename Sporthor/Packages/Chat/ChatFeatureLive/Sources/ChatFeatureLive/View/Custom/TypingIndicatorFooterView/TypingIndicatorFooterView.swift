import UIKit
import DesignKit
import MessageKit

final class TypingIndicatorFooterView: MessageReusableView {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = ColorName.backgroundWeak100.color
        view.layer.cornerRadius = 12
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var typingBubble: TypingBubble = {
        let bubble = TypingBubble()
        bubble.translatesAutoresizingMaskIntoConstraints = false
        return bubble
    }()
    
    private lazy var typingLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.font = UIFont.body04Compact
        label.textColor = ColorName.contentSoft600.color
        return label
    }()
    
    // MARK: - Initialization
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Public Methods
    
    func configure(with username: String) {
        typingLabel.text = "\(username) yazıyor"
        typingBubble.startAnimating()
        containerView.isHidden = false
        isHidden = false
    }
    
    func hide() {
        typingBubble.stopAnimating()
        typingLabel.text = nil
        containerView.isHidden = true
        isHidden = true
    }
    
    // MARK: - Private Methods
    
    private func setupUI() {
        backgroundColor = .clear
        
        addSubview(containerView)
        containerView.addSubview(typingLabel)
        containerView.addSubview(typingBubble)
        
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 12),
            containerView.trailingAnchor.constraint(lessThanOrEqualTo: trailingAnchor, constant: -12),
            containerView.topAnchor.constraint(equalTo: topAnchor),
            containerView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            typingLabel.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 12),
            typingLabel.centerYAnchor.constraint(equalTo: containerView.centerYAnchor),
            
            typingBubble.leadingAnchor.constraint(equalTo: typingLabel.trailingAnchor, constant: 8),
            typingBubble.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -12),
            typingBubble.centerYAnchor.constraint(equalTo: containerView.centerYAnchor),
            typingBubble.heightAnchor.constraint(equalToConstant: 16),
            typingBubble.widthAnchor.constraint(equalToConstant: 40)
        ])
        
        containerView.isHidden = true
    }
} 
