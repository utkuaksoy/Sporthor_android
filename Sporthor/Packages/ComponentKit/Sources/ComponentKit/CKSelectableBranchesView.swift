//
//  CKSelectableBranchesView.swift
//  ComponentKit
//
//  Created by derTurke on 10.04.2025.
//

import UIKit

public final class CKSelectableBranchesView: UIView {
    // MARK: - UI Elements
    private lazy var scrollView: UIScrollView = {
        let scroll = UIScrollView()
        scroll.showsHorizontalScrollIndicator = false
        scroll.translatesAutoresizingMaskIntoConstraints = false
        return scroll
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    distribution: .fill,
                                    spacing: 12)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var addButton: CKButton = {
        let button = CKButton(delegate: self)
        button.contentEdgeInsets = .init(top: 8, left: 16, bottom: 8, right: 16)
        button.widthAnchor.constraint(greaterThanOrEqualToConstant: 130).isActive = true
        return button
    }()
    
    // MARK: - Members
    private weak var delegate: CKSelectableBranchesViewDelegate?
    private lazy var tagsButtonTitleColor: UIColor = .clear
    private lazy var tagsButtonBackgroundColor: UIColor = .clear
    private lazy var tagsButtonFont: UIFont = .systemFont(ofSize: 14)
    private lazy var selectedTagsButtonTitleColor: UIColor = .clear
    private lazy var selectedTagsButtonBackgroundColor: UIColor = .clear
    private lazy var selectedTagsButtonFont: UIFont = .systemFont(ofSize: 14)
    private lazy var tagsButtonImageTitleSpacing: CGFloat = 0
    private lazy var buttonsCornerRadius: CGFloat = 0
    
    private lazy var tags: [(title: String, image: String, type: String, isSelected: Bool)] = [] {
        didSet {
            refreshTags()
        }
    }
    
    private var isFirstLoad = true
    
    // MARK: - Initialize
    public init(delegate: CKSelectableBranchesViewDelegate? = nil,
                tags: [(title: String, image: String, type: String, isSelected: Bool)] = [],
                tagsButtonTitleColor: UIColor = .clear,
                tagsButtonBackgroundColor: UIColor = .clear,
                tagsButtonFont: UIFont = .systemFont(ofSize: 14),
                selectedTagsButtonTitleColor: UIColor = .clear,
                selectedTagsButtonBackgroundColor: UIColor = .clear,
                selectedTagsButtonFont: UIFont = .systemFont(ofSize: 14),
                tagsButtonImageTitleSpacing: CGFloat = 0,
                isHiddenAddButton: Bool = false,
                addButtonImage: String = "",
                addButtonTitle: String = "",
                addButtonTitleColor: UIColor = .clear,
                addButtonTitleFont: UIFont = .systemFont(ofSize: 14),
                addButtonBackgroundColor: UIColor = .clear,
                addButtonImageTitleSpacing: CGFloat = 0,
                buttonsCornerRadius: CGFloat = 0) {
        super.init(frame: .zero)
        self.delegate = delegate
        self.tags = tags
        self.tagsButtonTitleColor = tagsButtonTitleColor
        self.tagsButtonBackgroundColor = tagsButtonBackgroundColor
        self.tagsButtonFont = tagsButtonFont
        self.selectedTagsButtonTitleColor = selectedTagsButtonTitleColor
        self.selectedTagsButtonBackgroundColor = selectedTagsButtonBackgroundColor
        self.selectedTagsButtonFont = selectedTagsButtonFont
        self.tagsButtonImageTitleSpacing = tagsButtonImageTitleSpacing
        
        self.addButton.isHidden = isHiddenAddButton
        self.addButton.setImage(UIImage(named: addButtonImage))
        self.addButton.setTitle(addButtonTitle)
        self.addButton.setTitleColor(addButtonTitleColor)
        self.addButton.setBackgroundColor(addButtonBackgroundColor)
        self.addButton.setImageTitleSpacing(addButtonImageTitleSpacing)
        self.addButton.setFont(addButtonTitleFont)
        self.addButton.setCornerRadius(buttonsCornerRadius)
        self.buttonsCornerRadius = buttonsCornerRadius
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        addSubview(scrollView)
        scrollView.addSubview(stackView)
        
        NSLayoutConstraint.activate([
            scrollView.topAnchor.constraint(equalTo: topAnchor),
            scrollView.leadingAnchor.constraint(equalTo: leadingAnchor),
            scrollView.trailingAnchor.constraint(equalTo: trailingAnchor),
            scrollView.bottomAnchor.constraint(equalTo: bottomAnchor),
            
            stackView.topAnchor.constraint(equalTo: scrollView.topAnchor),
            stackView.leadingAnchor.constraint(equalTo: scrollView.leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: scrollView.trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: scrollView.bottomAnchor),
            stackView.heightAnchor.constraint(equalTo: scrollView.heightAnchor),
        ])
    }
    
    // MARK: - Custom Methods
    public func bind(delegate: CKSelectableBranchesViewDelegate? = nil,
                     tags: [(title: String, image: String, type: String, isSelected: Bool)],
                     isHiddenAddButton: Bool = false) {
        self.delegate = delegate
        self.tags = tags
        self.addButton.isHidden = isHiddenAddButton
    }
    
    private func refreshTags() {
        self.stackView.removeAllArrangedSubviews()
        
        self.tags.forEach {
            let button = CKButton(
                title: $0.title,
                titleColor: $0.isSelected ? selectedTagsButtonTitleColor : tagsButtonTitleColor,
                buttonBackgroundColor: $0.isSelected ? selectedTagsButtonBackgroundColor : tagsButtonBackgroundColor,
                font: $0.isSelected ? selectedTagsButtonFont : tagsButtonFont,
                image: UIImageView().setImage(with: $0.image).resize(to: CGSize(width: 18, height: 18)))
            button.addTarget(self, action: #selector(tagsButtonClicked(_:)), for: .touchUpInside)
            button.setCornerRadius(buttonsCornerRadius)
            button.contentEdgeInsets = .init(top: 8, left: 16, bottom: 8, right: 16)
            button.restorationIdentifier = $0.type
            
            self.stackView.addArrangedSubview(button)
        }
        
        stackView.addArrangedSubview(addButton)
    }
    
    @objc private func tagsButtonClicked(_ sender: CKButton) {
        delegate?.tagButtonClicked(sender.restorationIdentifier ?? "")
    }
}

extension CKSelectableBranchesView: CKButtonDelegate {
    public func ckButtonDidTap(tag: Int) {
        delegate?.addButtonClicked()
    }
}
