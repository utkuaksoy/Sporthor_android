//
//  SubMenuCardView.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//

import UIKit
import ComponentKit

protocol SubMenuCardViewDelegate: AnyObject {
    func didTappedSubMenu(_ model: MenuModel)
}

final class SubMenuCardView: UIView {
    // MARK: - UI Elements
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.clipsToBounds = true
        imageView.setCornerRadius(20)
        imageView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        imageView.widthAnchor.constraint(equalToConstant: 40).isActive = true
        return imageView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .bold04Compact)
        return label
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    alignment: .center,
                                    spacing: 8,
                                    cornerRadius: 8,
                                    borderWidth: 1,
                                    borderColor: DesignKitColorName.borderSoft200.color)
        stackView.addArrangedSubviews([imageView, titleLabel])
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = UIEdgeInsets(top: 8, left: 8, bottom: 8, right: 8)
        stackView.isUserInteractionEnabled = true
        stackView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTappedSubMenu)))
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: SubMenuCardViewDelegate?
    private var subMenu: MenuModel?
    
    // MARK: - Initializers
    init(image: UIImage? = nil, title: String? = nil) {
        super.init(frame: .zero)
        prepareUI()
        imageView.image = image
        titleLabel.text = title
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
        imageView.image = nil
        titleLabel.text = nil
    }
    
    private func prepareUI() {
        addSubview(stackView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    func bind(delegate: SubMenuCardViewDelegate?, model: MenuModel) {
        self.delegate = delegate
        self.subMenu = model
        imageView.isHidden = model.iconPath.isEmpty
        imageView.setImage(with: model.iconPath)
        titleLabel.text = model.name
    }
    
    @objc private func didTappedSubMenu() {
        guard let subMenu else { return }
        delegate?.didTappedSubMenu(subMenu)
    }
}
