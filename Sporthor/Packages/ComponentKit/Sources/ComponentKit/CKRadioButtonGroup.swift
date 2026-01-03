//
//  CKRadioButtonGroup.swift
//  ComponentKit
//
//  Created by derTurke on 18.02.2025.
//

import UIKit
import DesignKit

// MARK: - CKRadioButtonGroup
public final class CKRadioButtonGroup: UIView {
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .vertical)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private var buttons: [CKButton] = []
    private var selectedIndex: Int?
    
    // MARK: - Members
    private weak var delegate: CKRadioButtonGroupDelegate?
    private var image: UIImage?
    private var selectedImage: UIImage?
    private var radioGroupSpacing: CGFloat = 0
    private var imageLabelSpacing: CGFloat = 0
    
    // MARK: - Initialize
    public init(delegate: CKRadioButtonGroupDelegate? = nil,
                options: [String] = [],
                selectedIndex: Int? = nil,
                radioGroupSpacing: CGFloat = 0,
                image: UIImage? = nil,
                selectedImage: UIImage? = nil,
                imageLabelSpacing: CGFloat = 0) {
        super.init(frame: .zero)
        setupView()
        self.bind(delegate: delegate,
                  options: options,
                  selectedIndex: selectedIndex,
                  radioGroupSpacing: radioGroupSpacing,
                  image: image,
                  selectedImage: selectedImage,
                  imageLabelSpacing: imageLabelSpacing)
    }
    
    public required init?(coder: NSCoder) {
        self.image = nil
        self.selectedImage = nil
        self.radioGroupSpacing = 0
        super.init(coder: coder)
        setupView()
    }
    
    // MARK: - Setup
    private func setupView() {
        addSubview(stackView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    public func bind(delegate: CKRadioButtonGroupDelegate? = nil,
                     options: [String],
                     selectedIndex: Int? = nil,
                     radioGroupSpacing: CGFloat = 0,
                     image: UIImage? = nil,
                     selectedImage: UIImage? = nil,
                     imageLabelSpacing: CGFloat = 0) {
        stackView.removeAllArrangedSubviews()
        buttons.removeAll()
        
        self.delegate = delegate
        self.selectedIndex = selectedIndex
        self.image = image
        self.selectedImage = selectedImage
        self.radioGroupSpacing = radioGroupSpacing
        self.stackView.spacing = radioGroupSpacing
        self.imageLabelSpacing = imageLabelSpacing
        
        
        for (index, option) in options.enumerated() {
            let button = createRadioButton(title: option, tag: index)
            buttons.append(button)
            stackView.addArrangedSubview(button)
        }
        
        updateAllButtons()
    }
    
    private func createRadioButton(title: String, tag: Int) -> CKButton {
        let button = CKButton()
        button.setTitle(title)
        button.setTitleColor(ColorName.contentStrong900.color)
        button.setFont(.body03Compact)
        button.setImageTitleSpacing(imageLabelSpacing)
        button.contentHorizontalAlignment = .left
        button.tag = tag
        button.addTarget(self, action: #selector(radioButtonTapped(_:)), for: .touchUpInside)
        updateButtonState(button)
        return button
    }
    
    private func updateAllButtons() {
        for button in buttons {
            updateButtonState(button)
        }
    }
    
    private func updateButtonState(_ button: CKButton) {
        let currentImage = button.tag == selectedIndex ? selectedImage : image
        button.setImage(currentImage)
    }
    
    @objc private func radioButtonTapped(_ sender: CKButton) {
        selectedIndex = sender.tag
        updateAllButtons()
        delegate?.radioButtonGroup(self, didSelect: sender.tag)
    }
}
