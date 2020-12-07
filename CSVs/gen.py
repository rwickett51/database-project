# Users: username, password
#
#
#
import uuid
import json
import csv
import random
import decimal

def generateCards():
    with open("./oracle-cards-20201130220551.txt", 'r', encoding="utf8") as file:
        data = file.read().replace('\n', '')
        f = json.loads(data)

        filename_set = './set.csv'
        filename_card = './card.csv'

        with open(filename_set, 'w', encoding="utf8") as csv_set:
            csvwriter_set = csv.writer(csv_set)
            csvwriter_set.writerow(['cardID', 'set', 'number'])

            with open(filename_card, 'w', encoding="utf8") as csv_card:
                csvwriter_card = csv.writer(csv_card)
                csvwriter_card.writerow(['name', 'id', 'cmc', 'type', 'subtype', 'text', 'power', 'toughness'])

                limit = 0
                #i = 104
                for i in f:
                    if limit == 1000: break
                    #print(json.dumps(i, indent=4))
                    name, id, cmd, type, subtype, text, power, toughness, set, number = '', '', '', '', '', '', '', '', '', ''

                    name = i['name']
                    cmc = str(i['cmc']).replace('.0', '')

                    id = str(limit)

                    if '—' in i['type_line']:
                        temp = i['type_line'].split('—')
                        type = temp[0].replace(' ', '')
                        subtype = temp[1].replace(' ', '')

                    else:
                        type = i['type_line']
                        subtype = 'None'

                    if 'oracle_text' in i:
                        text = i['oracle_text'].replace("\n", ' ')
                    else:
                        text = 'None'

                    if 'power' in i:
                        power = i['power']
                        toughness = i['toughness']
                    else:
                        power = '-999'
                        toughness = '-999'
                    set = i['set']
                    number = i['collector_number']

                    card_arr = [name, id, cmc, type, subtype, text, power, toughness]
                    set_arr = [id, set, number]
                    csvwriter_set.writerow(set_arr)
                    csvwriter_card.writerow(card_arr)

                    print(name)
                    limit += 1

def getCondition():
    value = random.randint(1, 5)
    if (value == 1):
        return 'Near Mint'
    elif (value == 2):
        return 'Lightly Played'
    elif (value == 3):
        return 'Moderately Played'
    elif (value == 4):
        return 'Heavily Played'
    elif (value == 5):
        return 'Damaged'

def printUsers(username, password):
    print('Users');
    print('Username: ' + username)
    print('Password: ' + password)
    print()

def printCart(username, cartID):
    print('Cart');
    print('username: ' + username)
    print('CartID: ' + cartID)
    print()

def generateUsers():

    filename_user = './user.csv'
    filename_cart = './cart.csv'

    with open(filename_user, 'w', encoding="utf8") as csv_user:
        csvwriter_user = csv.writer(csv_user)
        csvwriter_user.writerow(['username', 'password'])
        with open(filename_cart, 'w', encoding="utf8") as csv_cart:
            csvwriter_cart = csv.writer(csv_cart)
            csvwriter_cart.writerow(['username', 'id'])

            for x in range(250):
                print('////////////////////////////////////////////////////////////////////////')
                quantity = 0
                username = 'username' + str(x)
                password = 'password' + str(x)
                cartID = 'cart' + str(x)
                cardID = str(uuid.uuid1())
                printUsers(username, password)
                printCart(username, cartID)

                csvwriter_user.writerow([username, password])
                csvwriter_cart.writerow([username, cartID])

def generateListings():
    filename_user = './listings.csv'

    with open(filename_user, 'w', encoding="utf8") as csv_listings:
        csvwriter_listing = csv.writer(csv_listings)
        csvwriter_listing.writerow(['cardID', 'username', 'condition', 'price', 'id', 'inCart', 'isSold'])
        listingNum = 0
        for i in range(1000):
            print('/////////////////////////////////////////////////////////////')
            for j in range (5):

                cardID = i
                print(cardID)
                listingID = 'listing' + str(listingNum)
                print(listingID)
                username = 'username' + str(random.randint(0, 249))
                print(username)
                condition = getCondition()
                print(condition)
                price = '$' + str(decimal.Decimal(random.randrange(10000))/100)
                print(price)
                inCart = 0
                print(inCart)
                isSold = 0
                print(isSold)
                listingNum += 1
                print()
                csvwriter_listing.writerow([cardID, username, condition, price, listingID, 0, 0])


# Name, id, cmc, type, subtype, text, power, toughness, set, set number

if __name__ == '__main__':
    generateListings()
