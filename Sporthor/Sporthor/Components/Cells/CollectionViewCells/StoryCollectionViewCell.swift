//
//  StoryCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 26.03.2025.
//

import UIKit
import ComponentKit

protocol StoryCollectionViewCellDelegate: AnyObject {
    func didTapAddStoryButton()
    func didTapStoryProfile(model: Story)
}

final class StoryCollectionViewCell: UICollectionViewCell {
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var imageView: CKStoryProfileView = {
        let imageView = CKStoryProfileView(delegate: self,
                                           imageSize: 72)
        return imageView
    }()
    
    private lazy var addButton: CKButton = {
        let button = CKButton(delegate: self,
                              buttonBackgroundColor: .black,
                              cornerRadius: 16,
                              borderWidth: 4,
                              borderColor: .white,
                              image: Asset.whitePlus.image)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 32).isActive = true
        button.widthAnchor.constraint(equalToConstant: 32).isActive = true
        button.layer.zPosition = 9999
        return button
    }()
    
    private lazy var label: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            textAlignment: .center,
                            font: .body06Compact)
        return label
    }()
    
    // MARK: - Members
    private weak var delegate: StoryCollectionViewCellDelegate?
    private var model: Story?
    
    // MARK: - Initializers
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
    }
    
    private func setupUI() {
        imageView.addSubview(addButton)
        stackView.addArrangedSubviews([imageView, label])
        contentView.addSubview(stackView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: contentView.topAnchor),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            imageView.heightAnchor.constraint(equalToConstant: 82),
            imageView.widthAnchor.constraint(equalToConstant: 82),
            addButton.trailingAnchor.constraint(equalTo: imageView.trailingAnchor),
            addButton.bottomAnchor.constraint(equalTo: imageView.bottomAnchor)
        ])
    }
    // MARK: - Custom Methods
    func bind(delegate: StoryCollectionViewCellDelegate? = nil,
              model: Story) {
        self.delegate = delegate
        self.model = model
        
        imageView.setImage(url: model.profileImageUrl, placeholder: Asset.errorUserImage.image)
        imageView.layoutIfNeeded()
        let gradientColors: [UIColor]
        
        if model.details.isEmpty {
            gradientColors = [.clear, .clear]
        } else if model.isWatched {
            gradientColors = [
                DesignKitColorName.borderSoft200.color,
                DesignKitColorName.borderSoft200.color
            ]
        } else {
            gradientColors = [
                DesignKitColorName.backgroundPrimaryGreen.color,
                DesignKitColorName.primaryPurple.color,
                DesignKitColorName.primaryPink.color
            ]
        }
        imageView.removeGradientBorder()
        imageView.addGradientBorder(colors: gradientColors,
                                    borderWidth: 2,
                                    startPoint: CGPoint(x: 0.5, y: 0.0),
                                    endPoint: CGPoint(x: 0.5, y: 1.0))
        
        addButton.isHidden = !model.isOwn
        label.text = model.username
    }

}

extension StoryCollectionViewCell: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        guard let delegate else { return }
        delegate.didTapAddStoryButton()
    }
}

extension StoryCollectionViewCell: CKStoryProfileViewDelegate {
    func didTapStoryProfile() {
        guard let delegate,
              let model else { return }
        delegate.didTapStoryProfile(model: model)
    }
}
