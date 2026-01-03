//
//  CKPostActionView.swift
//  ComponentKit
//
//  Created by derTurke on 29.03.2025.
//

import UIKit
import DesignKit

public protocol CKPostActionViewDelegate: AnyObject {
    func didTappedActionButton(tag: Int)
}

public final class CKPostActionView: UIView {
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, spacing: 16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    
    // MARK: - Members
    private weak var delegate: CKPostActionViewDelegate?
    
    // MARK: - Initialize
    public override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        addSubview(stackView)
        NSLayoutConstraint.activate([
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    public func bind(delegate: CKPostActionViewDelegate? = nil,
                     actions: [(image: UIImage?, count: Int, tag: Int)]) {
        stackView.removeAllArrangedSubviews()
        self.delegate = delegate
        actions.forEach { (image, count, tag) in
            let imageTitleStackView = CKStackView(axis: .horizontal,
                                                  spacing: count == 0 ? 0 : 4)
            let imageView = UIImageView()
            imageView.contentMode = .scaleAspectFit
            imageView.image = image
            imageView.isUserInteractionEnabled = true
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedActionItem(_:)))
            imageView.addGestureRecognizer(tapGesture)
            imageView.tag = tag
            
            let label = CKLabel(text: "\(count)",
                                textColor: DesignKit.ColorName.contentStrong900.color,
                                font: .body06Compact,
                                tag: tag)
            label.isHidden = count == 0
            
            imageTitleStackView.addArrangedSubviews([imageView, label])
            
            stackView.addArrangedSubview(imageTitleStackView)
        }
    }
    
    @objc private func didTappedActionItem(_ sender: UITapGestureRecognizer) {
        delegate?.didTappedActionButton(tag: sender.view?.tag ?? 0)
    }
    
    public func updateImageAndCount(forTag tag: Int, newImage: UIImage?, newCount: Int) {
        for case let containerStack as UIStackView in stackView.arrangedSubviews {
            for (index, subview) in containerStack.arrangedSubviews.enumerated() {
                if let imageView = subview as? UIImageView, imageView.tag == tag {
                    imageView.image = newImage
                    if containerStack.arrangedSubviews.indices.contains(index + 1),
                       let label = containerStack.arrangedSubviews[index + 1] as? UILabel {
                        label.text = "\(newCount)"
                        label.isHidden = newCount == 0
                    }
                    return
                }
            }
        }
    }
}
