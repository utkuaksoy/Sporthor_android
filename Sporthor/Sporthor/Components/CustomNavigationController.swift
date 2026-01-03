//
//  CustomNavigationController.swift
//  Sporthor
//
//  Created by derTurke on 24.02.2025.
//

import UIKit
import ComponentKit

// MARK: - CustomNavigationControllerDelegate
protocol CustomNavigationControllerDelegate: AnyObject {
    func didTapButton(type: BarButtonItemType)
    func navigationBarSearchBarDidBeginEditing(_ searchBar: CKSearchBar)
    func navigationBarSearchBarTextDidChange(_ searchBar: CKSearchBar, text: String)
    func navigationBarSearchBarTextDidEndEditing(_ searchBar: CKSearchBar, text: String)
    func navigationBarSearchBarDidCancel(_ searchBar: CKSearchBar)
}

extension CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {}
    func navigationBarSearchBarDidBeginEditing(_ searchBar: CKSearchBar) {}
    func navigationBarSearchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {}
    func navigationBarSearchBarTextDidEndEditing(_ searchBar: CKSearchBar, text: String) {}
    func navigationBarSearchBarDidCancel(_ searchBar: CKSearchBar) {}
}

// MARK: - BarButtonItemLocation
enum BarButtonItemLocation {
    case left
    case right
}

// MARK: - BarButtonItemType
enum BarButtonItemType: Int {
    case back = 0
    case close
    case textRight
    case writeLabel
    case location
}

// MARK: - UINavigationController
final class CustomNavigationController: UINavigationController, UIGestureRecognizerDelegate {
    
    // MARK: -  Members
    weak var customDelegate: CustomNavigationControllerDelegate?
    
    var isBackExist: Bool = false {
        didSet {
            if isBackExist {
                prepareBackBarButtonItem()
            } else {
                removeBarButtonItem(location: .left, tag: .back)
            }
        }
    }
    
    var isBackChevronLeft: Bool = false {
        didSet {
            if isBackChevronLeft {
                prepareBackChevronLeftBarButtonItem()
            } else {
                removeBarButtonItem(location: .left, tag: .back)
            }
        }
    }
    
    var isBackWhiteExist: Bool = false {
        didSet {
            if isBackWhiteExist {
                prepareBackWhiteBarButtonItem()
            } else {
                removeBarButtonItem(location: .left, tag: .back)
            }
        }
    }
    
    var isCloseExist: Bool = false {
        didSet {
            if isCloseExist {
                prepareCloseBarButtonItem()
            } else {
                removeBarButtonItem(location: .left, tag: .close)
            }
        }
    }
    
    var isCloseWhiteExist: Bool = false {
        didSet {
            if isCloseWhiteExist {
                prepareWhiteCloseBarButtonItem()
            } else {
                removeBarButtonItem(location: .left, tag: .close)
            }
        }
    }
    
    var isCloseBackgroundBlackExist: Bool = false {
        didSet {
            if isCloseBackgroundBlackExist {
                prepareBackgroundBlackCloseBarButtonItem()
            } else {
                removeBarButtonItem(location: .left, tag: .close)
            }
        }
    }
    
    var searchBarTitleViewPlaceholder: String = "" {
        didSet {
            prepareTitleViewSearchBar()
        }
    }
    
    var isTextRightBarButtonItem: (text: String, textColor: UIColor, font: UIFont) = ("", .white, .systemFont(ofSize: 14)) {
        didSet {
            if !isTextRightBarButtonItem.text.isEmpty {
                prepareTextRightBarButtonItem()
            } else {
                removeBarButtonItem(location: .right, tag: .textRight)
            }
        }
    }
    
    var isRemoveTextRightBarButtonItem: Bool = false {
        didSet {
            if isRemoveTextRightBarButtonItem {
                removeBarButtonItem(location: .right, tag: .textRight)
            }
        }
    }
    
    var isWriteLabelExist: Bool = false {
        didSet {
            if isWriteLabelExist {
                prepareWriteLabelBarButtonItem()
            } else {
                removeBarButtonItem(location: .right, tag: .writeLabel)
            }
        }
    }
    
    var isLocationExist: Bool = false {
        didSet {
            if isLocationExist {
                prepareLocationBarButtonItem()
            } else {
                removeBarButtonItem(location: .right, tag: .location)
            }
        }
    }
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        setupNavigationBar()
        setupBackButton()
        
        interactivePopGestureRecognizer?.delegate = self
    }
    
    // MARK: - Custom Methods
    func setupNavigationBar() {
        let backImage = Asset.back.image
        
        let appearance = UINavigationBar.appearance()
        
        appearance.backIndicatorImage = backImage
        appearance.backIndicatorTransitionMaskImage = backImage
        appearance.tintColor = .clear
        appearance.isTranslucent = true
        appearance.setBackgroundImage(UIImage(), for: .default)
        appearance.shadowImage = UIImage()
    }
    
    private func setupBackButton() {
        topViewController?.navigationItem.backButtonTitle = ""
    }
    
    func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        return viewControllers.count > 1
    }
    
    private func setupBarButtonItem(image: UIImage,
                                    selector: Selector?,
                                    location: BarButtonItemLocation,
                                    tag: BarButtonItemType) {
        let barButtonItem = UIBarButtonItem(image: image,
                                            style: .plain,
                                            target: self,
                                            action: selector)
        barButtonItem.tag = tag.rawValue
        appendBarButtonItem(barButtonItem, location: location)
    }
    
    private func setupCustomBarButtonItem(customView: UIView,
                                          location: BarButtonItemLocation,
                                          tag: BarButtonItemType) {
        let barButtonItem = UIBarButtonItem(customView: customView)
        barButtonItem.tag = tag.rawValue
        appendBarButtonItem(barButtonItem, location: location)
    }
    
    private func appendBarButtonItem(_ item: UIBarButtonItem, location: BarButtonItemLocation) {
        switch location {
        case .left:
            if topViewController?.navigationItem.leftBarButtonItems == nil {
                topViewController?.navigationItem.leftBarButtonItems = []
            }
            topViewController?.navigationItem.leftBarButtonItems?.append(item)
        case .right:
            if topViewController?.navigationItem.rightBarButtonItems == nil {
                topViewController?.navigationItem.rightBarButtonItems = []
            }
            topViewController?.navigationItem.rightBarButtonItems?.append(item)
        }
    }
    
    private func removeBarButtonItem(location: BarButtonItemLocation, tag: BarButtonItemType) {
        switch location {
        case .left:
            topViewController?.navigationItem.leftBarButtonItems?.removeAll(where: { $0.tag == tag.rawValue })
        case .right:
            topViewController?.navigationItem.rightBarButtonItems?.removeAll(where: { $0.tag == tag.rawValue })
        }
    }

    private func prepareBackBarButtonItem() {
        setupBarButtonItem(image: Asset.back.image,
                           selector: #selector(didTapButton(_:)),
                           location: .left,
                           tag: .back)
    }
    
    private func prepareBackChevronLeftBarButtonItem() {
        setupBarButtonItem(image: Asset.chevronLeftIcon.image,
                           selector: #selector(didTapButton(_:)),
                           location: .left,
                           tag: .back)
    }
    
    private func prepareBackWhiteBarButtonItem() {
        setupBarButtonItem(image: Asset.chevronLeftWhiteIcon.image,
                           selector: #selector(didTapButton(_:)),
                           location: .left,
                           tag: .back)
    }
    
    @objc private func didTapButton(_ sender: UIBarButtonItem) {
        guard let customDelegate,
              let type = BarButtonItemType(rawValue: sender.tag) else { return }
        customDelegate.didTapButton(type: type)
    }
    
    private func prepareTitleViewSearchBar() {
        let searchBar = CKSearchBar(delegate: self,
                                    textColor: DesignKitColorName.contentStrong900.color,
                                    placeholder: searchBarTitleViewPlaceholder,
                                    placeholderColor: DesignKitColorName.contentSoft600.color,
                                    backgroundColor: DesignKitColorName.contentWeak100.color,
                                    cornerRadius: 22,
                                    borderWidth: 1,
                                    selectedBorderColor: DesignKitColorName.borderSub300.color,
                                    font: .body04Compact,
                                    image: Asset.searchbarSearch.image,
                                    clearImage: Asset.searchbarClose.image,
                                    cancelButtonTitle: "İptal",
                                    cancelButtonTitleColor: DesignKitColorName.contentStrong900.color,
                                    cancelButtonFont: .body04Compact)
        searchBar.translatesAutoresizingMaskIntoConstraints = false
        searchBar.widthAnchor.constraint(equalToConstant: view.frame.size.width).isActive = true
        searchBar.heightAnchor.constraint(equalToConstant: 50).isActive = true
        topViewController?.navigationItem.titleView = searchBar
    }
    
    func searchBarChangeText(_ text: String) {
        if let searchBar = topViewController?.navigationItem.titleView as? CKSearchBar {
            searchBar.changeText(text)
        }
    }
    
    private func prepareCloseBarButtonItem() {
        setupBarButtonItem(image: Asset.closeGrey.image,
                           selector: #selector(didTapButton(_:)),
                           location: .left,
                           tag: .close)
    }
    
    private func prepareWhiteCloseBarButtonItem() {
        setupBarButtonItem(image: Asset.closeWhite.image,
                           selector: #selector(didTapButton(_:)),
                           location: .left,
                           tag: .close)
    }
    
    private func prepareBackgroundBlackCloseBarButtonItem() {
        setupBarButtonItem(image: Asset.closeBackgroundBlack.image,
                           selector: #selector(didTapButton(_:)),
                           location: .left,
                           tag: .close)
    }
    
    private func prepareTextRightBarButtonItem() {
        let label = CKLabel(
            delegate: self,
            text: isTextRightBarButtonItem.text,
            textColor: isTextRightBarButtonItem.textColor,
            font: isTextRightBarButtonItem.font,
            isUserInteractionEnabled: true,
            tag: BarButtonItemType.textRight.rawValue
        )
        setupCustomBarButtonItem(
            customView: label,
            location: .right,
            tag: .textRight
        )
    }
    
    private func prepareWriteLabelBarButtonItem() {
        setupBarButtonItem(image: Asset.characterButton.image,
                           selector: #selector(didTapButton(_:)),
                           location: .right,
                           tag: .writeLabel)
    }
    
    private func prepareLocationBarButtonItem() {
        setupBarButtonItem(image: Asset.storyPin.image,
                           selector: #selector(didTapButton(_:)),
                           location: .right,
                           tag: .location)
    }
}

// MARK: - CKSearchBarDelegate
extension CustomNavigationController: CKSearchBarDelegate {
    func searchBarDidBeginEditing(_ searchBar: CKSearchBar) {
        customDelegate?.navigationBarSearchBarDidBeginEditing(searchBar)
    }
    
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        customDelegate?.navigationBarSearchBarTextDidChange(searchBar, text: text)
    }
    
    func searchBarDidCancel(_ searchBar: CKSearchBar) {
        customDelegate?.navigationBarSearchBarDidCancel(searchBar)
    }
    
    func searchBarTextDidEndEditing(_ searchBar: CKSearchBar, text: String) {
        customDelegate?.navigationBarSearchBarTextDidEndEditing(searchBar, text: text)
    }
}

extension CustomNavigationController: CKLabelDelegate {
    func didTapCKLabel(tag: Int) {
        switch tag {
        case BarButtonItemType.textRight.rawValue:
            customDelegate?.didTapButton(type: .textRight)
        default:
            break
        }
    }
}
