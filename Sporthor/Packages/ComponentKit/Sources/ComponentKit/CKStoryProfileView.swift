//
//  CKStoryProfileView.swift
//  ComponentKit
//
//  Created by derTurke on 19.03.2025.
//

import UIKit

public final class CKStoryProfileView: UIView {
    // MARK: - UI Elements
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.isUserInteractionEnabled = true
        imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTappedImage(_:))))
        return imageView
    }()
    
    // MARK: - Members
    private var imageSize: CGFloat = 0
    private weak var delegate: CKStoryProfileViewDelegate?
    
    // MARK: - Initialize
    public init(delegate: CKStoryProfileViewDelegate? = nil,
                hasStory: Bool = false,
                image: UIImage? = nil,
                imageSize: CGFloat = 0,
                colors: [UIColor] = [],
                borderWidth: CGFloat = 0,
                startPoint: CGPoint = .zero,
                endPoint: CGPoint = .zero) {
        self.delegate = delegate
        self.imageSize = imageSize
        super.init(frame: .zero)
        setImage(image)
        imageView.setCornerRadius(imageSize / 2)
        addGradientBorder(colors: hasStory ? colors : [],
                          borderWidth: borderWidth,
                          startPoint: startPoint,
                          endPoint: endPoint)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupView() {
        addSubview(imageView)
        
        NSLayoutConstraint.activate([
            imageView.centerXAnchor.constraint(equalTo: centerXAnchor),
            imageView.centerYAnchor.constraint(equalTo: centerYAnchor),
            imageView.widthAnchor.constraint(equalToConstant: imageSize),
            imageView.heightAnchor.constraint(equalToConstant: imageSize)
        ])
    }
    
    // MARK: - Custom Methods
    public func setImage(_ image: UIImage?,
                         contentMode: UIView.ContentMode = .scaleAspectFill) {
        imageView.image = image
        imageView.contentMode = contentMode
    }
    
    public func setImage(url: String?, placeholder: UIImage? = nil) {
        imageView.setImage(with: url, placeholder: placeholder)
    }
    
    @objc private func didTappedImage(_ sender: UITapGestureRecognizer) {
        delegate?.didTapStoryProfile()
    }
}
